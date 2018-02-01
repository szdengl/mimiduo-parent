package net.mimiduo.boot.common.service.impl;

import com.google.common.base.Objects;
import net.mimiduo.boot.common.service.RuntimePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RuntimePropertyServiceImpl implements RuntimePropertyService {




}
