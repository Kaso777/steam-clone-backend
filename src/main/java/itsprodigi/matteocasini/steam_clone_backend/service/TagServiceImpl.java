package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.TagDTO;
import itsprodigi.matteocasini.steam_clone_backend.model.Tag;
import itsprodigi.matteocasini.steam_clone_backend.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Optional<TagDTO> getTagById(Long id) {
        return tagRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public Optional<TagDTO> getTagByName(String name) {
        return tagRepository.findByName(name).map(this::convertToDto);
    }

    @Override
    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TagDTO createOrGetTag(TagDTO tagDTO) {
        return tagRepository.findByName(tagDTO.getName())
                .map(this::convertToDto)
                .orElseGet(() -> convertToDto(tagRepository.save(new Tag(tagDTO.getName()))));
    }

    @Override
    @Transactional
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag non trovato con ID: " + id));
        tag.setName(tagDTO.getName());
        return convertToDto(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    private TagDTO convertToDto(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}