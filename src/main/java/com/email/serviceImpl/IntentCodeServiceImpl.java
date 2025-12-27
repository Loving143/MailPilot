package com.email.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.email.master.IntentCategory;
import com.email.master.IntentCode;
import com.email.repository.IntentCategoryRepository;
import com.email.repository.IntentCodeRepository;
import com.email.request.IntentCodeRequest;
import com.email.resposne.IntentCodeResponse;
import com.email.service.IntentCodeService;

@Service
public class IntentCodeServiceImpl implements IntentCodeService {

	@Autowired
    private IntentCodeRepository intentRepo;

    @Autowired
    private IntentCategoryRepository categoryRepo;

    public IntentCode createIntent(IntentCodeRequest req) {

        IntentCategory category = categoryRepo
                .findByCategoryCode(req.getCategoryCode())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (intentRepo.findByCode(req.getCode()).isPresent()) {
            throw new RuntimeException("Intent code already exists");
        }

        IntentCode intent = new IntentCode();
        intent.setCode(req.getCode());
        intent.setTitle(req.getTitle());
        intent.setDescription(req.getDescription());
        intent.setSubjectTemplate(req.getSubjectTemplate());
        intent.setBodyTemplate(req.getBodyTemplate());
        intent.setCategory(category);

        return intentRepo.save(intent);
    }

    public List<IntentCode> getIntentsByCategory(String categoryCode) {
        return intentRepo.findByCategory_CategoryCode(categoryCode);
    }

    public IntentCode getIntentByCode(String code) {
        return intentRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Intent not found"));
    }
	
	@Override
	public IntentCodeResponse fetchIntentCode(String Code) {
		IntentCode intentCode = intentRepo.findByCode(Code).orElseThrow(()-> new RuntimeException("Intent code with the given code does not exists!"));
		IntentCodeResponse res = new IntentCodeResponse(intentCode);
		return res;
	}

	@Override
	public IntentCode registerIntentCode(IntentCodeRequest req) {
		IntentCategory category = categoryRepo
                .findByCategoryCode(req.getCategoryCode())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (intentRepo.findByCode(req.getCode()).isPresent()) {
            throw new RuntimeException("Intent code already exists");
        }

        IntentCode intent = new IntentCode();
        intent.setCode(req.getCode());
        intent.setTitle(req.getTitle());
        intent.setDescription(req.getDescription());
        intent.setSubjectTemplate(req.getSubjectTemplate());
        intent.setBodyTemplate(req.getBodyTemplate());
        intent.setCategory(category);
        return intentRepo.save(intent);
	}

}
