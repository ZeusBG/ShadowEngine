uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform float power;
uniform float screenHeight;
uniform float scale;
void main() {
	vec2 position;
        position.x = gl_FragCoord.x/1920;
        position.y = gl_FragCoord.y/1080;
        float distance = length(lightLocation - gl_FragCoord.xy);
        distance/=scale;
        float attenuation;
        
        float lastThreshold = 0.0005-50/100000.0;

        if(distance<50)
            attenuation = power*0.0005;
        else if(distance<100)
            attenuation = power*0.0005-(distance-50)/100000;
        else{
            attenuation = power*lastThreshold-(distance-100)/100000;
        }
        
        
	vec4 color = vec4(attenuation, attenuation, attenuation,0.5) * vec4(lightColor, 1);

	gl_FragColor = color;
}