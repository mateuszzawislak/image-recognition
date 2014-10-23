Image Recognition
=================

Detection of radioactive sites by recognizing the symbols of radioactivity in picture.

The idea was to enable the recognition of symbols in images taken at different levels of quality. In addition, the program should be "possibly insensitive" on rotation, tilt and size of the searched symbol, which is associated with an increase in the span of acceptable values of the individual factors describing the elements of the symbol.

Solution scheme
--------------
1. Improving the quality of the image.
2. Binarization of the image.
3. Segmentation of the image.
4. The calculation of segments' parameters.
5. Extracting elements resembling searched symbol.
6. Finding whole symbols of radioactivity.

Implementation
--------------
Project is realized using the JAVA programming language without external libraries dedicated to pattern recognition. GUI was developed using the popular framework *Spring*. Beyond the recognition program allows numerous operations on the selected image. GUI  enables filtering, binarization, and many other was to facilitate the evaluation of the results of an experimental, partial operations that form the whole recognition algorithm.
