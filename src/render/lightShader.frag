uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform float power;
uniform float screenHeight;
uniform float scale;
void main() {
	float distance = length(lightLocation - gl_FragCoord.xy);
        distance/=scale/3;
	float attenuation = power*0.02 /distance;
        
        
	vec4 color = vec4(attenuation, attenuation, attenuation,0.5) * vec4(lightColor, 1);

	gl_FragColor = color;
}