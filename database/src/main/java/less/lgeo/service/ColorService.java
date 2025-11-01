package less.lgeo.service;

import java.util.List;
import java.util.Optional;

import less.lgeo.common.Color;
import less.lgeo.entity.ColorEntity;
import less.lgeo.repository.ColorRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Fetches {@link ColorEntity} from the database
 */
@Service
public class ColorService {

  private final ColorRepository colorRepository;

  public ColorService(ColorRepository colorRepository) {
    this.colorRepository = colorRepository;
  }

  public List<ColorEntity> getAllColors() {
    return colorRepository.findAll();
  }

  /**
   * @return database entity by Color Code(Id), Null if no corresponding color is
   *         found
   */
  public Optional<ColorEntity> getColorByCode(Integer colorCode) {
    return colorRepository.findById(colorCode);
  }

  public void deleteColorById(@NonNull Integer id) {
    colorRepository.deleteById(id);
  }
}
