uniform sampler2D u_TextureSampler0;


varying highp vec4  vColors;  
varying highp float  surfaceAlpha;

varying highp vec2   TexCoord0;


void main()
{
 
    highp vec4 base = texture2D(u_TextureSampler0, TexCoord0) ;
    highp vec4 mask = texture2D(u_TextureSampler0, vec2(TexCoord0.x+0.5,TexCoord0.y)) ;
    base.a = mask.r;
   // base.rgb += (base.rgb * (1.0 - base.a));
  //   base.rgb = clamp( base.rgb,0.0,1.0);
   

    gl_FragColor = base * surfaceAlpha * vColors;
    //gl_FragColor = vec4(mask.rgb,1.0);
}    
  