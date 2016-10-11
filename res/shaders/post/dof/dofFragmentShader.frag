#version 150

in vec2 texCoords;

uniform sampler2D colorTexture;
uniform sampler2D depthTexture;

uniform float focusRange;
uniform float focusDistance;
uniform float xScale;
uniform float yScale;

uniform float near;
uniform float far;

vec2 nearFar = vec2(near, far);

out vec4 colorOut;

/*
* Effect based on JME's Depth of Field effect
*/

void main() {
    vec4 texVal = texture(colorTexture, texCoords);

    float zBuffer = texture(depthTexture, texCoords).r;

    float a = nearFar.y / (nearFar.y - nearFar.x);
    float b = nearFar.y * nearFar.x / (nearFar.x - nearFar.y);
    float z = b / (zBuffer - a);

    float unFocus = min(1.0, abs(z - focusDistance) / focusRange);

    if (unFocus < 0.2) {
        colorOut = texVal;
    } else {
        // Perform a wide convolution filter and we scatter it
        // a bit to avoid some texture look-ups.  Instead of
        // a full 5x5 (25-1 lookups) we'll skip every other one
        // to only perform 12.
        // 1  0  1  0  1
        // 0  1  0  1  0
        // 1  0  x  0  1
        // 0  1  0  1  0
        // 1  0  1  0  1

        vec4 sum = vec4(0.0);
        float x = texCoords.x;
        float y = texCoords.y;

        // In order from lower left to right, depending on how you look at it
        sum += texture( colorTexture, vec2(x - 2.0 * xScale, y - 2.0 * yScale) );
        sum += texture( colorTexture, vec2(x - 0.0 * xScale, y - 2.0 * yScale) );
        sum += texture( colorTexture, vec2(x + 2.0 * xScale, y - 2.0 * yScale) );
        sum += texture( colorTexture, vec2(x - 1.0 * xScale, y - 1.0 * yScale) );
        sum += texture( colorTexture, vec2(x + 1.0 * xScale, y - 1.0 * yScale) );
        sum += texture( colorTexture, vec2(x - 2.0 * xScale, y - 0.0 * yScale) );
        sum += texture( colorTexture, vec2(x + 2.0 * xScale, y - 0.0 * yScale) );
        sum += texture( colorTexture, vec2(x - 1.0 * xScale, y + 1.0 * yScale) );
        sum += texture( colorTexture, vec2(x + 1.0 * xScale, y + 1.0 * yScale) );
        sum += texture( colorTexture, vec2(x - 2.0 * xScale, y + 2.0 * yScale) );
        sum += texture( colorTexture, vec2(x - 0.0 * xScale, y + 2.0 * yScale) );
        sum += texture( colorTexture, vec2(x + 2.0 * xScale, y + 2.0 * yScale) );

        sum = sum / 12.0;
        colorOut = mix(texVal, sum, unFocus);

         colorOut.r = unFocus;
    }
}