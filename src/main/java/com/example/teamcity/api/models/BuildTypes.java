package com.example.teamcity.api.models;

import com.example.teamcity.api.annotations.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildTypes  extends BaseModel{
    @Optional
    private int count;
    @Optional
    private List<BuildType> buildType;
}
