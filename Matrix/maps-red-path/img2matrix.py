import cv2
import numpy as np
import pandas as pd
import csv
import os

# Nombre de pixels dans l'image
MAX_PIXELS = 3043 * 4011  # 1500*1200

ROOT_DIR = os.getcwd() + "\\" 

DIR = ROOT_DIR + "plan-masse\\"

ETAGES = ["RDC"]

SIZE = "3043x4011"

matrix_filename = "\\matrix2img.png"

def increase_brightness(args):
    hsv = cv2.cvtColor(args["image"], cv2.COLOR_BGR2HSV)
    h, s, v = cv2.split(hsv)

    lim = 255 - args["value"]
    v[v > lim] = 255
    v[v <= lim] += args["value"]

    final_hsv = cv2.merge((h, s, v))
    return cv2.cvtColor(final_hsv, cv2.COLOR_HSV2BGR)

def shrink(args):
    width, height = args["image"].shape[0], args["image"].shape[1]
    c = np.sqrt(args["max_pixels"] / (width * height))
    width = int(width * c)
    height = int(height * c)
    dim = (height, width)
    return cv2.resize(args["image"], dim, interpolation=cv2.INTER_AREA), c

# Conversion d'un image noir/blanc en matrice binaire
def map2mat(file, max_pixels, pixel_value):
    bright_img = increase_brightness({"image": cv2.imread(file), "value": 255})
    shrunk_img, c = shrink({"image": bright_img,
                                     "max_pixels": max_pixels})
    gray = cv2.cvtColor(shrunk_img, cv2.COLOR_BGR2GRAY)
    el = cv2.cvtColor(gray, cv2.COLOR_GRAY2BGR)
    hsv = cv2.cvtColor(el, cv2.COLOR_BGR2HSV)
    h, s, v = cv2.split(hsv)
    v[v < 255] = 245
    s[v < 255] = 255
    el = cv2.cvtColor(cv2.merge((h, s, v)), cv2.COLOR_HSV2BGR)

    mat = np.array(gray.copy())
    mat[mat != 255] = pixel_value
    mat[mat == 255] = 0
    return mat, c, el

def split(word):
    return [char for char in word]

# Vérifier les coordonnées
def test_csv(dataFile, dataFile_csv):
    lines = []
    error = False
    for line in open(dataFile, "r"):
        elts = split(line.strip())
        lines.append(elts)
    df = pd.DataFrame(lines)
    for line in open(dataFile_csv, "r"):
        line_1 = line.strip().split(";")
        x = int(line_1[-2])
        y = int(line_1[-1])

        if x == 0 and y == 0:
            continue
        if int(df.iloc[x][y]) == 0:
            print(">>>>", line)
            error = True
    return error


for ETAGE in ETAGES:
    # Lire et réduire la taille du plan masse
    map_file = DIR + ETAGE + "\\plan-masse-" + SIZE + ".png"
    path_file = DIR + ETAGE + "\\plan-masse-chemin-" + SIZE + ".png"
    shrunk_nopath_img, _ = shrink({"image": cv2.imread(map_file), "max_pixels": MAX_PIXELS})
    mat_path, c, path = map2mat(path_file, pixel_value=1, max_pixels=MAX_PIXELS)
    mat_inter, _, inters = map2mat(DIR + ETAGE + "\\plan-masse-intersections-" + SIZE + ".png", pixel_value=2,
                                       max_pixels=MAX_PIXELS)
    mat = mat_path + mat_inter
    mat[mat == 2] = 0  # Enlever les points d'intersection qui ne touche pas le chemin
    mat[mat == 3] = 2  # Changement de la valeur des points d'intersection

    # Préparer les noms des fichiers
    height, width, _ = shrunk_nopath_img.shape
    shrunk_size = str(width) + "x" + str(height)
    dst = ROOT_DIR + "plan-masse-" + shrunk_size + "-" + ETAGE
    dst_coor = dst + "\\coordonées-plan-masse-" + shrunk_size + ".csv"
    dst_matrix = dst + "\\plan-masse-chemin-matrice-" + shrunk_size + ".txt"
    dst_shrunk_nopath_img = dst + "\\plan-masse-" + shrunk_size + ".png"
    dst_shrunk_points_inters_img = dst + "\\plan-masse-chemin-intersections-points-" + shrunk_size + ".png"

    # Enregistrer la matrice
    cv2.imwrite(dst_shrunk_nopath_img, shrunk_nopath_img)
    print("==>", dst_shrunk_nopath_img)

    # Ajouter les points sur le plan masse rétrécit
    df = pd.read_csv(DIR + "complet\\coordonées-plan-masse-" + SIZE + ".csv", delimiter=";")
    for i in range(len(df)):
        x = df.iloc[i]['x']
        y = df.iloc[i]['y']
        if x + y != 0:
            y = int(y * c) + 1
            x = int(x * c)
        if ETAGE == df.iloc[i]['niveau']:
            shrunk_nopath_img[y][x] = [255, 0, 0]

    # Créer un plan masse aves les points et les intersections des chemins
    shrunk_points_img = cv2.addWeighted(path, 0.15, shrunk_nopath_img, 0.85, 0)
    shrunk_points_inter_img = cv2.addWeighted(inters, 0.15, shrunk_points_img, 0.85, 0)

    # Créer le répertoire de destination
    if not os.path.isdir(dst):
        os.mkdir(dst)

    # Enregistrer les nouvelles coordonnées
    columns = list(df.columns)
    points = []
    for i in range(len(df)):
        x = df.iloc[i]['x']
        y = df.iloc[i]['y']
        if x + y != 0:
            y = int(y * c)
            x = int(x * c)
        points.append([df.iloc[i]["batiment"], df.iloc[i]["niveau"],
                       df.iloc[i]["id"], df.iloc[i]["description"], y, x])
    data = pd.DataFrame(points, columns=columns)
    data.to_csv(dst_coor, index=False, sep=";", header=False)
    print("==>", dst_coor)

    # Enregistrer le plan masse réduit, et le plan masse réduit avec les points
    np.savetxt(dst_matrix, mat, delimiter='', fmt='%.0f')
    print("==>", dst_matrix)
    mat[mat == 0] = 255
    mat[mat == 1] = 0
    mat[mat == 2] = 0
    rgb = cv2.cvtColor(mat, cv2.COLOR_GRAY2RGB)
    for i in range(len(df)):
        x = df.iloc[i]['x']
        y = df.iloc[i]['y']
        if x + y != 0:
            y = int(y * c)
            x = int(x * c)
            rgb[y][x] = [0, 0, 255]
    cv2.imwrite(dst + matrix_filename, rgb)
    cv2.imwrite(dst_shrunk_points_inters_img, cv2.addWeighted(path, 0.15, shrunk_points_inter_img, 0.85, 0))
    print("==>", dst_shrunk_points_inters_img)
    test_csv(dst_matrix, dst_coor)
