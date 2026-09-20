package com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionRepositoryContractTest {

    @Test
    void usesTheLongTypeOfTheTransactionEntityIdentifier() {
        Type repositoryType = TransactionRepository.class.getGenericInterfaces()[0];

        assertThat(repositoryType).isInstanceOf(ParameterizedType.class);
        Type[] typeArguments = ((ParameterizedType) repositoryType).getActualTypeArguments();

        assertThat(typeArguments).hasSize(2);
        assertThat(typeArguments[1].getTypeName()).isEqualTo("java.lang.Long");
    }
}
