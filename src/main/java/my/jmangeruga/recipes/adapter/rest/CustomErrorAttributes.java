package my.jmangeruga.recipes.adapter.rest;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.springframework.web.util.WebUtils.ERROR_STATUS_CODE_ATTRIBUTE;

@Component
class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final ErrorAttributeOptions options) {
        final Integer status = getAttribute(webRequest, ERROR_STATUS_CODE_ATTRIBUTE);
        return new SimpleErrorRs(HttpStatus.valueOf(status).getReasonPhrase(), null).asMap();
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, 0);
    }

}
