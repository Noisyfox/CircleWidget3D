uniform sampler2D u_TextureSampler0;


precision highp float;
varying       vec2 g_vTexture0;

varying highp vec3 SpecularLight;
varying  vec2  g_vTexture1; 




varying  highp float    offset;


varying highp vec4 vColors;


void main() {
    highp vec4 texColor  = texture2D(u_TextureSampler0, g_vTexture0) * vColors;
  //  highp vec4 shadowMask  = texture2D(u_TextureSampler0, g_vTexture1);
  //  texColor.rgb +=  shadowMask.rgb * 0.7;

    gl_FragColor = texColor; 
   

 
}
