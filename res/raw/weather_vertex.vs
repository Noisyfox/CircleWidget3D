attribute highp   vec3  a_Position;
attribute highp vec3  a_Normals;
attribute highp vec2  a_TexCoords;

uniform highp mat4  u_MVPMatrix;
uniform highp mat4  u_MVMatrix;
uniform highp mat3  u_MVITMatrix;
uniform highp mat4  u_UVMatrix;
uniform highp vec3  u_LightDirection0;
uniform highp vec3  u_LightColor0;
uniform int u_LightVisible0;
uniform highp vec3  u_LightDirection1;
uniform highp vec3  u_LightColor1;
uniform int u_LightVisible1;
uniform highp vec4  u_LightPosition2;
uniform highp vec3  u_LightColor2;
uniform int u_LightVisible2;

uniform highp vec3  u_LightDirection3;
uniform int u_LightVisible3;

uniform highp vec3  u_AmbientColor;
uniform highp vec4  u_DiffuseColor;
uniform highp vec3  u_SpecularColor;
uniform highp float  u_Shininess;

highp float  Light0Intensity;


varying highp    vec3  SpecularLight;  
varying highp    vec4  DiffLight;
varying highp    vec3  SpecularLight2;  
varying highp    vec3  DiffLight2;
varying highp    vec4 rimLight;
varying highp float  alphaSpec;
varying highp float  surfaceAlpha;
varying highp vec2   TexCoord0;
varying highp vec2   TexCoord1;




highp vec4  vPositionES;
highp vec3  vNormalES;

void main()
{
    // Transform position
    gl_Position = u_MVPMatrix * vec4(a_Position, 1.0);    
    vPositionES = u_MVMatrix * vec4(a_Position,1.0);
    vNormalES =   normalize(u_MVITMatrix *  a_Normals);
    highp vec3  eyeDir = -normalize(vPositionES.xyz);


    // Pass through texcoords and animate them if needed
    highp vec2  textcenter = a_TexCoords + vec2(0.5,0.5);
    highp vec2 textcentertransformed = vec2 (u_UVMatrix *  vec4(textcenter,0.0,1.0));  
    TexCoord0 = textcentertransformed - vec2(0.5,0.5);
    TexCoord1 = a_TexCoords;
  //  vColors = a_Colors;
    surfaceAlpha = u_DiffuseColor.a;


    highp vec3 SpecularLight0 = vec3(0.0);
    highp vec3 SpecularLight1 = vec3(0.0);
    SpecularLight2 = vec3(0.0);
    rimLight = vec4(0.0);
    highp    vec3  DiffLight0;  
    highp    vec3  DiffLight1;  
 
    alphaSpec = 0.0;
 
     Light0Intensity = max(dot(vNormalES, u_LightDirection0),0.0); 
     DiffLight0 = u_AmbientColor + Light0Intensity;       
     DiffLight0 *= vec3(u_DiffuseColor.rgb);
     DiffLight0 *= u_LightColor0;
     if (Light0Intensity > 0.0) {  
        highp vec3 halfVector = normalize(u_LightDirection0 + eyeDir);  
        highp float NdotH = max(dot(vNormalES, halfVector), 0.0);    
        highp float specular = pow(NdotH, u_Shininess);
        alphaSpec += specular;
        SpecularLight0 += specular ;
        SpecularLight0 *= u_SpecularColor;
        SpecularLight0 *= u_LightColor0;

     }


     
     Light0Intensity = max(dot(vNormalES, u_LightDirection1),0.0);
     DiffLight1 = u_AmbientColor + Light0Intensity;  
     DiffLight1 *= vec3(u_DiffuseColor.rgb);
     DiffLight1 *= u_LightColor1;
     if (Light0Intensity > 0.0) {
        highp vec3 halfVector = normalize(u_LightDirection1 + eyeDir);
        highp float NdotH = max(dot(vNormalES, halfVector), 0.0);    
        highp float specular = pow(NdotH, u_Shininess);
        alphaSpec += specular;
        SpecularLight1 += specular ;
        SpecularLight1 *= u_SpecularColor;
        SpecularLight1 *= u_LightColor1;
    }


      
   if (u_LightVisible2  == 1) {    
    highp vec3 Light2Direction = normalize( u_LightPosition2.xyz - vPositionES.xyz );
    Light0Intensity = max(dot(vNormalES, Light2Direction),0.0);
    DiffLight2 = u_AmbientColor + Light0Intensity; 
    DiffLight2 *= vec3(u_DiffuseColor.rgb);
    DiffLight2 *= u_LightColor2;
   // DiffLight2 = vec3(Light0Intensity,Light0Intensity,Light0Intensity); 
  
    if (Light0Intensity > 0.0) {
        highp vec3 halfVector = normalize(Light2Direction + eyeDir);
        highp float NdotH = max(dot(vNormalES, halfVector), 0.0);    
        highp float specular = pow(NdotH, u_Shininess);
        alphaSpec += specular;        
        SpecularLight2 += specular ;
        SpecularLight2 *= u_SpecularColor;
        SpecularLight2 *= u_LightColor2;
      
    }
     }
     else {
        DiffLight2 = vec3(0.0);
     }  
     
     
   if (u_LightVisible3  == 1) {        
    Light0Intensity = max(dot(vNormalES, u_LightDirection3),0.0);
    rimLight = vec4(u_LightDirection3,1.0) ;   
    if (Light0Intensity > 0.0) {
        highp vec3 halfVector = normalize(u_LightDirection3 + eyeDir);
        highp float NdotH = max(dot(vNormalES, halfVector), 0.0);    
        highp float specular = pow(NdotH, 32.0);
        rimLight.a = specular  * 2.0;        

    }
    }
 

     SpecularLight = SpecularLight0 + SpecularLight1 ;
     DiffLight = clamp(vec4(DiffLight0 + DiffLight1 , 1.0), 0.0,1.0);
 
  
    
}