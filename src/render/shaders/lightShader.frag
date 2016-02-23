uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform float power;
uniform vec2 scale;
uniform vec2 screenSize;
uniform vec2 cameraCenter;
uniform float halfRadius;

void main() {
		vec2 position;
        position.x = gl_FragCoord.x/scale.x - cameraCenter.x;
        position.y = (screenSize.y - gl_FragCoord.y/scale.y) - cameraCenter.y;
		
        float distance = length(lightLocation - position);
		
        float attenuation;

        if(distance<halfRadius)
            attenuation = power*0.0005;
        else 
            attenuation = power*0.0005-(distance-halfRadius)/110000.0;
        

        
	vec4 color = vec4(attenuation, attenuation, attenuation,0.5) * vec4(lightColor, 1);

	gl_FragColor = color;
}