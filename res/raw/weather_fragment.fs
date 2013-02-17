uniform sampler2D u_TextureSampler0;
uniform sampler2D u_TextureSampler1;
uniform sampler2D u_TextureSampler2;


varying highp    vec4  DiffLight;  

varying highp    vec3  SpecularLight;

//varying highp vec4  vColors;  
varying highp float  surfaceAlpha;
varying highp float  alphaSpec; 
varying highp    vec4 rimLight;
varying highp    vec3  SpecularLight2;  
varying highp    vec3  DiffLight2;



varying highp vec2   TexCoord0;
varying highp vec2   TexCoord1;


void main()   
{
     highp vec3 Spec = SpecularLight + SpecularLight2 ;
     highp vec4 Diff = clamp(vec4(DiffLight.rgb + DiffLight2 , 1.0), 0.0,1.0);
    highp vec4 base = texture2D(u_TextureSampler0, TexCoord0) ; 
    highp vec4  mask = texture2D(u_TextureSampler1, TexCoord1) ;   
    highp vec4  rim = texture2D(u_TextureSampler2, TexCoord1) ; 
    base.a = mask.r;
 
 	base.rgb += (mask.b - 0.5) * 2.0;
    base.a += alphaSpec * mask.g ;
    base = clamp(base,0.0,1.0);
    base *= Diff ;
    base += vec4(Spec,0.0) * mask.g;

    base += rim.b * rimLight.aaaa;
    base.a *= surfaceAlpha;

    gl_FragColor = base;


 
   

  
    
}