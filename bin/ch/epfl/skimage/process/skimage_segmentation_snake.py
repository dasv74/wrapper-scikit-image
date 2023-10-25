## command: skimage.segmentation.active_contour

import sys
import numpy as np
import skimage.io as io
from skimage.filters import gaussian
from skimage.segmentation import active_contour


n = len(sys.argv)
f = open("log.txt", "w")
print("python " + sys.argv[0] + " " + sys.argv[1] + " " + sys.argv[2] + " " + sys.argv[3] + " " + sys.argv[4])

if n >= 6:
    f.write("input: " + sys.argv[1] + "\n")
    f.write("output: " + sys.argv[2] + "\n")
    f.write("init: " + sys.argv[3] + "\n")
    f.write("w_line: " + sys.argv[4] + "\n")
    f.write("w_edge: " + sys.argv[5] + "\n")
    
    with open(sys.argv[3]) as f:
        lines = f.readlines()
    n = len(lines)
    init = np.zeros((n,2))
    for i in range(n):
        a = lines[i].partition(',')
        init[i] = [float(a[0]), float(a[2])]    
    input = io.imread(sys.argv[1])
    cntr = active_contour(input, init, alpha=0.015, beta=10, gamma=0.001, w_line=float(sys.argv[4]), w_edge=float(sys.argv[5]), boundary_condition='periodic')
    a = np.asarray(cntr)
    np.savetxt(sys.argv[2], a, delimiter=",")
else:
    f.write("Error: the command should have 6 parameters")
f.close()   



cntr = active_contour(gaussian(input, 3),init, alpha=0.015, beta=10, gamma=0.001)
a = np.asarray(cntr)
np.savetxt('/Users/dsage/Desktop/output.csv', a, delimiter=",")