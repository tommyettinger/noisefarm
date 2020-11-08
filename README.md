# noisefarm
Artisanal, locally-sourced, organic continuous noise textures, served fresh and always cruelty-free.

Really, this is just a huge pile of seamless continuous noise textures, meant to be used by any code or art project that might
need seamlessly-tiling 2D noise. There are several types of noise in the `generated/256x256` folder (larger textures should be
coming soon): 

  - `value` has the lowest quality but still looks decent with enough octaves. It has large blocks of solid colors that fade into
    their neighbors, but with low octaves it is quite obviously an artificial formation.
  - `perlin` is a big step up from `value`, even though it's still constructed on a grid; because it effectively places a random
    vector instead of a flat block of color at each grid vertex, it looks much smoother.
  - `simplex` is usually a step up from `perlin`, simply because it uses a triangular grid and that makes linear artifacts harder
    to notice. If you're generating noise in real time, Simplex noise is the fastest of these, but it loses quality if you need
    6-dimensional or higher noise (6D noise is mostly used to make seamlessly-tiling 3D cubes).
  - `honey` combines `value` and `simplex` and accentuates high and low values; this helps avoid the artifacts that `value` has,
    while also avoiding the characteristic "high-low-high-low" value pattern that `simplex` and `perlin` have. It improves
    considerably with 2 or 3 octaves.
  - `foam` is an especially unusual and high-quality type of noise; it's probably the most natural-looking here. It is produced by
    getting 5 different `value` results, where each noise call is rotated differently in 4D space, and each noise result shifts
    the inputs to the next, before all the results are averaged and the highs and lows are accentuated. It changes its character
    as octaves are added, starting with noticeable light and dark sections, but quickly becoming a fairly realistic cloud. The
    version here tends to look more "defined" with higher frequency.
    

