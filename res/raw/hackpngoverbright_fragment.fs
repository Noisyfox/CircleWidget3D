uniform sampler2D u_TextureSampler0;

varying highp vec4  vColors;  
varying highp float  surfaceAlpha;

varying highp vec2   TexCoord0;


void main()
{
 
    highp vec4 base = texture2D(u_TextureSampler0, TexCoord0) ;
    base.rgb += (base.rgb * (1.0 - base.a));
  //   base.rgb = clamp( base.rgb,0.0,1.0);
   

    gl_FragColor = base;
   // gl_FragColor = vec4(1.0,0.0,0.0,1.0);
}    
  