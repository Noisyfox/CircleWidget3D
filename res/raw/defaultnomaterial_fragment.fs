uniform sampler2D u_TextureSampler0;


varying highp vec4  vColors;  
varying highp float  surfaceAlpha;

varying highp vec2   TexCoord0;


void main()
{

    gl_FragColor = texture2D(u_TextureSampler0, TexCoord0) * surfaceAlpha * vColors;
    
   
}    
  