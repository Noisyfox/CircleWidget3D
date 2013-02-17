attribute highp   vec3  a_Position;
attribute highp vec4 a_Colors;
attribute highp vec2  a_TexCoords;

uniform highp mat4  u_MVPMatrix;
uniform highp vec4  u_DiffuseColor;

varying highp vec4  vColors;  
varying highp float  surfaceAlpha;
varying highp vec2   TexCoord0;

highp vec4  vPositionES;


void main()
{
    // Transform position
    gl_Position = u_MVPMatrix * vec4(a_Position, 1.0);    
    TexCoord0 = a_TexCoords;
    surfaceAlpha = u_DiffuseColor.a;
    vColors = a_Colors;   
}