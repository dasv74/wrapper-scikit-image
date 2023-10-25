## command: skimage.filters.gaussian
## 5 parameters: input_file output_file sigma mode

import sys
import skimage.io as io
import skimage.filters as filters
n = len(sys.argv)
f = open("log.txt", "w")
print("python " + sys.argv[0] + " " + sys.argv[1] + " " + sys.argv[2] + " " + sys.argv[3] + " " + sys.argv[4])
if n >= 5:
    f.write("input: " + sys.argv[1] + "\n")
    f.write("output: " + sys.argv[2] + "\n")
    f.write("sigma: " + sys.argv[3] + "\n")
    f.write("mode: " + sys.argv[4] + "\n")
    input = io.imread(sys.argv[1])
    output = filters.gaussian(input, sigma=float(sys.argv[3]), output=None, mode=sys.argv[4])
    a = io.imsave(sys.argv[2], output)
else:
    f.write("Error: the command should have 5 parameters")
f.close()   