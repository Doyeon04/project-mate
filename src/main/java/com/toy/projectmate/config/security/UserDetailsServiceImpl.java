package com.toy.projectmate.config.security;

import com.toy.projectmate.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final MemberRepository memberRepository;

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
      *//*  return (UserDetails) memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));*//*
        return memberRepository.getByUid(username);
    }*/
    @Override
    public UserDetails loadUserByUsername(String username) {
        LOGGER.info("[loadUserByUsername] loadUserByUsername 수행. username : {}", username);
        return memberRepository.getByEmail(username);
      //  return memberRepository.getByUid(username);
    }
}
