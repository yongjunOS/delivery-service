package org.delivery.api.domain.token.converter;


import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Converter;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.controller.TokenResponse;
import org.delivery.api.domain.token.model.TokenDto;

import java.util.Objects;

@RequiredArgsConstructor
@Converter
public class TokenConverter {


    public TokenResponse toResponse(
            TokenDto accessToken,
            TokenDto refreshToken
    ) {
        Objects.requireNonNull(accessToken, () -> {
            throw new ApiException(ErrorCode.NULL_POINT_ERROR_ERROR);
        });
        Objects.requireNonNull(refreshToken, () -> {
            throw new ApiException(ErrorCode.NULL_POINT_ERROR_ERROR);
        });

        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();
    }
}
