package io.github.pedrohribeiross.lancecerto.configuration;

import io.github.pedrohribeiross.lancecerto.shared.exception.InvalidPaginationException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CustomPageableResolver extends PageableHandlerMethodArgumentResolver {

    private static final int MAX_SIZE = 500;

    public CustomPageableResolver() {
        this.setMaxPageSize(MAX_SIZE);
    }

    @Override
    public @NonNull Pageable resolveArgument(MethodParameter methodParameter, @Nullable ModelAndViewContainer mavContainer,
                                             NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
        String sizeParam = webRequest.getParameter(getSizeParameterName());
        String pageParam = webRequest.getParameter(getPageParameterName());

        if(sizeParam != null) {
            try {
                int size = Integer.parseInt(sizeParam);
                if(size <= 0 ||  size > MAX_SIZE) {
                    throw new InvalidPaginationException("Tamanho de página inválido. Use entre 1 e " + MAX_SIZE);
                }
            }catch (NumberFormatException ex) {
                throw new InvalidPaginationException("O parâmetro 'size' deve ser um número inteiro");
            }
        }

        if(pageParam != null) {
            try {
                int page = Integer.parseInt(pageParam);
                if(page < 0) {
                    throw new InvalidPaginationException("Paginação não pode ser negativo");
                }
            }catch (NumberFormatException ex) {
                throw new InvalidPaginationException("O parâmetro 'page' deve ser um número inteiro");
            }
        }

        return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }
}
