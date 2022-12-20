package com.minimi.domain.user.response;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PostInfoForPaging {
    Long cursorId;
    @Builder.Default
    List<PostInfoForm> postInfoFormList = new ArrayList<>();
}
