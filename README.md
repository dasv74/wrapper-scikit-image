# Wrapper scikit-image for FIJI

*written by Daniel Sage, EPFL. 23 October 2023*


These Java wrappers allow to call Python scripts from a ImageJ plugin. The images are exchange by files.

### Current wrappers
- Gaussian filter
- Segmentation by snake (active_contour)

### Installation

*require the installation of conda (e.g. miniconda)*

conda create -n scikit-image-env
conda scikit-image-env
pip install scikit-image

### Setup

- Select the virtual environment: /path/.../scikit-image-env/
- Select a temporary directory to exchange documents



