package com.ndtdoanh.JSSGradle.domain.response.file;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUploadFileDTO {
  private String fileName;
  private Instant uploadedAt;
}
