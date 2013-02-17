uniform sampler2D u_TextureSampler0;


precision highp float;
varying       vec2 g_vTexture0;

varying  vec2  g_vTexture1; 


varying highp vec4 vColors;


void main() {

    highp vec4 texColor  = texture2D(u_TextureSampler0, g_vTexture0) * vColors;
   // highp vec4 shadowMask  = texture2D(u_TextureSampler0, g_vTexture1);
   
   // texColor.a +=  shadowMask.a * 0.5;

    gl_FragColor = texColor * vColors; 
   

 
}
