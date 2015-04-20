#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying      vec2 v_texCoord0;

uniform float     u_time;
uniform float     u_pulse;
uniform sampler2D u_texture;
uniform vec2      u_resolution;
uniform vec2      u_screenPos;

void main() {
	LOWP vec4 vColor = v_color;
	LOWP vec4 texColor = texture2D(u_texture, v_texCoord0);
	LOWP vec3 color = vec3(0.0);
	float len = 0.0;
	float z = u_time;
	float pulse = u_pulse;

	vec2 st = gl_FragCoord.xy / u_resolution;
	vec2 pos = u_screenPos / u_resolution - vec2(0.5);
	vec2 p = st - pos;
	p -= 0.5;
	p.x *= u_resolution.x / u_resolution.y;
	len = length(p);

	for (int i = 0; i < 3; ++i) {
		vec2 uv = vec2(0.0);
		z += 0.7;
		uv += p / len * (sin(z) + 1.0) * abs(sin(len * 9.0 - z * 2.0));

		color[i] = 0.01 / length(abs(mod(uv, 1.0) - 0.5));
	}

	//gl_FragColor = texColor * vec4(color / len, z) + vec4(0.4, 0.4, 0.4, 1.0);
	gl_FragColor = texColor + vec4(color / len, z);
}