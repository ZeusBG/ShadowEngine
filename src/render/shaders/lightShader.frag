uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform float power;
uniform float screenHeight;
uniform float scale;
uniform float radius;

void main() {
	vec2 position;
        position.x = gl_FragCoord.x/1920;
        position.y = gl_FragCoord.y/1080;
        float distance = length(lightLocation - gl_FragCoord.xy);
        distance/=scale;
        float attenuation;
        


        if(distance<50)
            attenuation = power*0.001;
        else 
            attenuation = power*0.001-(distance-50)/110000.0;
        
        
        
	vec4 color = vec4(attenuation, attenuation, attenuation,0.5) * vec4(lightColor, 1);

	gl_FragColor = color;
}