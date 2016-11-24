package com.stayfprod.github.util;

import com.stayfprod.github.api.ApiClient;
import com.stayfprod.github.model.ErrorBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {
    public static ErrorBody parseError(Response<?> response) {
        Converter<ResponseBody, ErrorBody> converter =
                ApiClient.RETROFIT.responseBodyConverter(ErrorBody.class, new Annotation[0]);

        ErrorBody error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorBody();
        }

        return error;
    }
}
