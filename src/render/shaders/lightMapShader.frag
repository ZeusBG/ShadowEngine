uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform vec3 objectColor;
uniform vec2 screenSize;
uniform sampler2D backbuffer;
uniform float lightRadius;
uniform vec2 scale;
uniform vec2 cameraCenter;
uniform float power;
uniform float halfRadius;

void main() {
		
		vec2 position;
        position.x = gl_FragCoord.x/scale.x - cameraCenter.x;
        position.y = (screenSize.y - gl_FragCoord.y/scale.y) - cameraCenter.y;
		
		vec2 positionTex;
		positionTex.x = gl_FragCoord.x/1920.0;
		positionTex.y = gl_FragCoord.y/1080.0;
		
        vec4 texcolor = texture2D(backbuffer, positionTex);
		
        float distance = length(position - lightLocation);
        float attenuation = 0;
		
		if(distance<halfRadius)
            attenuation = 1;
        else {
			attenuation = 1 - (distance - halfRadius)/halfRadius;
			
		}
	vec4 color = vec4(texcolor.x+attenuation, texcolor.y+attenuation, texcolor.z+attenuation,1.0f);

	gl_FragColor = color;
}