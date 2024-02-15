package com.example.pro.auth.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.files.FileUploader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.example.pro.auth.service.MemberService.PROFILE_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private Member member;
    private MultipartFile file;
    @InjectMocks
    private MemberService memberService;
    @Mock
    private AuthService authService;
    @Mock
    private FileUploader fileUploader;

    @BeforeEach
    void init() {
        member = Member.builder()
                .username("username")
                .password("password1234")
                .nickname("nickname")
                .email("helloworld@gmail.com")
                .build();

        file = new MockMultipartFile("ForTest", new byte[]{});
    }

    @Test
    @DisplayName("[성공] 프로필 이미지 수정")
    void updateProfile() {
        Assertions.assertNull(member.getProfile());
        when(authService.loadUser()).thenReturn(member);
        when(fileUploader.uploadFile(file, PROFILE_KEY))
                .thenReturn("https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/ForTest.jpeg");
        Member updatedMember = memberService.updateProfile(file);
        assertThat(updatedMember.getProfile()).isNotBlank();
    }
}