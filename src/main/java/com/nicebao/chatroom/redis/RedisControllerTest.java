package com.nicebao.chatroom.redis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("redis")
public class RedisControllerTest {

	private final RedisTemplate redisTemplate;

	public RedisControllerTest(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@GetMapping("save")
	public void save(String key, String value){
		redisTemplate.opsForValue().set(key, value);
	}

}
