package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.EmailCampaign;
import com.fitness.core.auth.port.out.IEmailCampaignRepositoryPort;
import com.fitness.infrastructure.auth.entity.EmailCampaignEntity;
import com.fitness.infrastructure.auth.repository.EmailCampaignJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmailCampaignMysqlAdapter implements IEmailCampaignRepositoryPort {

    // Repository thao tác với bảng email_campaigns
    private final EmailCampaignJpaRepository repo;

    // JdbcTemplate dùng để query danh sách email người nhận
    private final JdbcTemplate jdbcTemplate;

    @Override
    public EmailCampaign save(EmailCampaign campaign) {

        // Chuyển Domain object sang Entity để lưu xuống database
        EmailCampaignEntity entity = EmailCampaignEntity.builder()
                .id(campaign.getId())
                .title(campaign.getTitle())
                .subject(campaign.getSubject())
                .htmlContent(campaign.getHtmlContent())
                .targetAudience(campaign.getTargetAudience())
                .status(campaign.getStatus())
                .build();

        // Lưu chiến dịch email vào database
        repo.save(entity);

        return campaign;
    }

    @Override
    public Optional<EmailCampaign> findById(UUID id) {

        // Tìm chiến dịch email theo ID và map từ Entity về Domain
        return repo.findById(id)
                .map(entity -> EmailCampaign.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .subject(entity.getSubject())
                        .htmlContent(entity.getHtmlContent())
                        .targetAudience(entity.getTargetAudience())
                        .status(entity.getStatus())
                        .build());
    }

    @Override
    public void updateStatus(UUID id, String status) {

        // Tìm chiến dịch cần cập nhật trạng thái
        EmailCampaignEntity entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chiến dịch marketing"));

        // Cập nhật trạng thái mới (VD: DRAFT → SENT)
        entity.setStatus(status);

        // Lưu lại thay đổi
        repo.save(entity);
    }

    @Override
    public List<String> getTargetEmails(String targetAudience) {
        //  Sử dụng INNER JOIN để chỉ quét đúng những tài khoản có quyền ROLE_MEMBER và đang Active
        String sql = "SELECT u.email FROM users u " +
                "JOIN user_roles ur ON u.id = ur.user_id " +
                "JOIN roles r ON ur.role_id = r.id " +
                "WHERE u.status = 'Active' AND r.name = 'ROLE_MEMBER'";

        return jdbcTemplate.queryForList(sql, String.class);
    }
}