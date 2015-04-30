#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying      vec2 v_texCoord0;

uniform float     u_time;
uniform float     u_timer;
uniform sampler2D u_texture;
uniform vec2      u_resolution;

const float duration = 3.0;
const float rings = 5.0;	//exactly the number of complete white rings at any moment.
const float velocity=4.;
const float b = 0.003;		//size of the smoothed border

void main()
{
	vec4 col = texture2D(u_texture, vec2(0.0, 0.0));
	vec2 position = gl_FragCoord.xy/u_resolution.xy;
	float aspect = u_resolution.x/u_resolution.y;
	position.x *= aspect;
	float dist = distance(position, vec2(aspect*0.5, 0.5));
	float offset=u_time*velocity;
	float conv=rings*4.;
	float v=dist*conv-offset;
	float ringr=floor(v);
	float color=smoothstep(-b, b, abs(dist- (ringr+float(fract(v)>0.5)+offset)/conv));
	if(mod(ringr,2.)==1.)
		color=1.-color;
	LOWP vec4 texColor = texture2D(u_texture, v_texCoord0);
	gl_FragColor = texColor + u_timer * vec4(color, color, color, color * texColor.a);
}
