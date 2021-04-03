package com.emag.model.dto.productimagedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteProductImagesDTO {

    @NotEmpty(message = "Images ids are mandatory")
    private List<Integer> imagesIds;
}
