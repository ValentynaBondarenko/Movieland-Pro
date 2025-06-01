package com.bondarenko.movieland.service.country;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.service.AbstractITest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

class CountryServiceImplTest extends AbstractITest {
    @Autowired
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        DataSourceListener.reset();
    }

    @Test
    @DisplayName("Should return countries matching given IDs from DB")
    void findByIdIn_shouldReturnCountriesWithMatchingIds() {
        Set<Long> requestedIds = Set.of(1L, 3L);

        Set<Country> countries = countryService.findByIdIn(requestedIds);

        Assertions.assertNotNull(countries);
        Assertions.assertEquals(2, countries.size());
        Assertions.assertTrue(countries.stream().anyMatch(c -> c.getId().equals(1L)));
        Assertions.assertTrue(countries.stream().anyMatch(c -> c.getId().equals(3L)));

        Assertions.assertEquals(0, DataSourceListener.getInsertCount());
    }
}
