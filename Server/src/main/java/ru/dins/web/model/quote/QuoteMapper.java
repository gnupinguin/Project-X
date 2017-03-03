package ru.dins.web.model.quote;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS;

class QuoteMapper extends ObjectMapper {

    QuoteMapper() {
        super();
        StdTypeResolverBuilder typeResolverBuilder = new DefaultTypeResolverBuilder(NON_CONCRETE_AND_ARRAYS);
        typeResolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        typeResolverBuilder.init(JsonTypeInfo.Id.NAME,
                new ClassNameIdResolver(this.getTypeFactory().constructType(Object.class),
                        TypeFactory.defaultInstance()));

        this.setDefaultTyping(typeResolverBuilder);
    }
}