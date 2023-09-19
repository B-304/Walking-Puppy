package io.ssafy.p.j9b304.backend.domain.walk.service;

import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Theme;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.ssafy.p.j9b304.backend.domain.walk.repository.ThemeRepository;
import io.ssafy.p.j9b304.backend.domain.walk.repository.WalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WalkService {
    private final WalkRepository walkRepository;
    private final ThemeRepository themeRepository;

    public Walk addWalkNewPath(/* User user, */ WalkAddRequestDto walkAddRequestDto) {
        Optional<Theme> optionalTheme = themeRepository.findById(walkAddRequestDto.getThemeId());
        if (optionalTheme.isEmpty()) {
            throw new RuntimeException();
        }

        Theme theme = optionalTheme.get();
        Walk walk = walkAddRequestDto.toEntity(walkAddRequestDto, theme);
//        walk.setUser(user);

        Walk newWalk = walkRepository.save(walk);
        return newWalk;
    }
}
