package com.fisolution.multiDatabase.hyun.app.repository;

import com.fisolution.multiDatabase.hyun.Member;

public interface UseDynamicProxy {
    Member mainSave(Member member);

    Member secondSave(Member member);
}
