package com.toy.projectmate.domain.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toy.projectmate.domain.BaseTimeEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Member implements UserDetails {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=45, nullable = false, unique = true)
    private String email;

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Column(length=45, nullable=false)
    private String nickname;

    @Column(length=100)
    private String password;

   /* @Enumerated(EnumType.STRING)
    private Role role;*/

    @ElementCollection(fetch= FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

 /*   public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // 계정이 가지고 있는 권한 목록을 리턴
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
