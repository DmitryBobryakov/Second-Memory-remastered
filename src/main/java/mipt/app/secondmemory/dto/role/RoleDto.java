package mipt.app.secondmemory.dto.role;

import mipt.app.secondmemory.entity.RoleType;

public record RoleDto(String email, Long fileId, RoleType Role) {}
