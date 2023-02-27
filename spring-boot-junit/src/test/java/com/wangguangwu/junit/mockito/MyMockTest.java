package com.wangguangwu.junit.mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * @author wangguangwu
 */
@ExtendWith(MockitoExtension.class)
class MyMockTest {

    @Mock
    private List<String> mockedList;

    @Test
    void testMockList() {
        Mockito.when(mockedList.get(0)).thenReturn("first");
        Mockito.when(mockedList.get(1)).thenThrow(new RuntimeException());

        Assertions.assertEquals("first", mockedList.get(0));

        Assertions.assertThrows(RuntimeException.class, () -> mockedList.get(1));

        Mockito.verify(mockedList, Mockito.times(2)).get(ArgumentMatchers.anyInt());
    }
}
