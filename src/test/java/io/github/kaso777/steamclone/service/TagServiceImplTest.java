package io.github.kaso777.steamclone.service;

import io.github.kaso777.steamclone.dto.TagDTO;
import io.github.kaso777.steamclone.model.Tag;
import io.github.kaso777.steamclone.repository.TagRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTags_returnsTagDTOList() {
        Tag tag = new Tag();
        tag.setId(1L); // Usa Long, non UUID
        tag.setName("Action");

        when(tagRepository.findAll()).thenReturn(List.of(tag));

        List<TagDTO> result = tagService.getAllTags();

        assertEquals(1, result.size());
        assertEquals("Action", result.get(0).getName());
    }
}
