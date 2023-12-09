package net.slc.dv.enums;

public enum Role {
	STUDENT("Student"),
	TEACHER("Teacher");

	private final String role;
	Role(String role) {
		this.role = role;
	}

	public String getString() {
		return role;
	}

	public static Role getRole(String role) {
		for (Role r : Role.values()) {
			if (r.getString().equals(role)) {
				return r;
			}
		}
		return null;
	}
}
