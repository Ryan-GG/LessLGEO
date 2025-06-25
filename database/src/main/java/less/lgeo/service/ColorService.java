package less.lgeo.service;

import java.util.List;
import java.util.Optional;
import less.lgeo.entity.ColorEntity;
import less.lgeo.repository.ColorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class ColorService {

  private static final Logger logger = LoggerFactory.getLogger(ColorService.class);

  private final ColorRepository colorRepository;

  public ColorService(ColorRepository colorRepository) {
    this.colorRepository = colorRepository;
  }

  public List<ColorEntity> getAllColors() {
    return colorRepository.findAll();
  }

  public @Nullable ColorEntity getColorByCode(Integer colorCode) {
    Optional<ColorEntity> optionalColor = colorRepository.findById(colorCode);
    if (optionalColor.isPresent()) {
      return optionalColor.get();
    }
    logger.info("Color with code: {} doesn't exist", colorCode);
    return null;
  }
}
