attribute vec3 a_Position;
attribute vec3 a_Normals;
attribute vec4 a_Colors;
attribute vec2 a_TexCoords;
uniform  mat4 u_MVPMatrix;
uniform  mat4 u_MVMatrix;
uniform  mat3 u_MVITMatrix;
uniform highp mat4  u_UVMatrix;

vec4  vPositionES;
vec3  vLightDir;
vec3  vNormalES;


    
highp vec2   shadowDir;
highp vec2 shadowUV;

varying  vec2  g_vTexture0; 
varying  vec2  g_vTexture1; 

varying  vec4  vColors;

highp float    offset;



void main() {
    gl_Position = u_MVPMatrix * vec4(a_Position,1.0);
    vPositionES = u_MVMatrix * vec4(a_Position,1.0);
    vNormalES = u_MVITMatrix *  a_Normals;
 
     highp vec2  textcenter = a_TexCoords + vec2(0.5,0.5);
    highp vec2 textcentertransformed = vec2 (u_UVMatrix *  vec4(textcenter,0.0,1.0));  
    g_vTexture0 = textcentertransformed - vec2(0.5,0.5);
    
    vColors = a_Colors;



    offset =  0.0078125 * 0.5;
   

    shadowDir = -vNormalES.xy;
   


    shadowUV = vec2(offset  * shadowDir.x, offset * shadowDir.y ) ;
    g_vTexture1 = g_vTexture0 + shadowUV;
}
