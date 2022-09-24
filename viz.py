#!/usr/bin/env python3

import numpy as np
import matplotlib.pyplot as plt

x = [278.0, 109.52371029579483, 86.02917665210413, 259.19427381701706, 258.51717102013936]
y = [278.0, 3.539112905926004, 122.41913483762059, 555.0, 557.2045141256829]
z = [-10.0, 128.01953972506342, 224.20428043400497, 350.07328907967315, 354.50968956566686]
 
 
fig = plt.figure()
ax = plt.axes(projection ='3d')
ax.plot(x, y, z, color="green")
ax.set_xlim3d([ 50, 350 ])
ax.set_ylim3d([ 250, 600 ])
ax.set_zlim3d([ -25, 400 ])
plt.show()
