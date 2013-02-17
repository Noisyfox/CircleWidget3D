uniform sampler2D u_TextureSampler0;
uniform sampler2D u_TextureSampler1;
uniform sampler2D u_TextureSampler2;
uniform sampler2D u_TextureSampler3;
precision highp float;
varying       vec2 g_vTexture0;

varying highp vec3 DiffuseLight;
varying highp vec3 SpecularLight;

varying  highp vec4  rimLight;
varying highp vec4 vColors;
varying  highp float   alphaSpec;
varying  highp float   SurfaceAlpha;
uniform  float u_Texture0Alpha;


varying  vec2  g_vTexture1;

void main() {    

    highp vec4 specular;
    
    highp float texture3Alpha = (1.0 - u_Texture0Alpha);
    highp vec4 texColor  = texture2D(u_TextureSampler2, g_vTexture0);
    highp vec4 mask  = texture2D(u_TextureSampler1, g_vTexture0);
    highp vec4 content;   
  	content = (texture2D(u_TextureSampler0, g_vTexture0) * u_Texture0Alpha) + 
  	     (texture2D(u_TextureSampler3, g_vTexture0) * texture3Alpha);
   	
    

    highp vec4 shadowMask  = (texture2D(u_TextureSampler0, g_vTexture1) *u_Texture0Alpha) + 
    (texture2D(u_TextureSampler3, g_vTexture1) *texture3Alpha);
    texColor.a = mask.r + (alphaSpec * mask.g) + content.a;

    specular = vec4 (SpecularLight * mask.g,0.0) ;
  
    highp vec4 diffuse = vec4(DiffuseLight,1.0);

    gl_FragColor.rgb = ((content.rgb * content.a) + (texColor.rgb * (1.0 - content.a)));
    gl_FragColor.a = texColor.a; 
    gl_FragColor = (gl_FragColor * diffuse + specular)  * vColors;
    gl_FragColor += mask.b * rimLight.aaaa;
    gl_FragColor.a *= SurfaceAlpha;
}
