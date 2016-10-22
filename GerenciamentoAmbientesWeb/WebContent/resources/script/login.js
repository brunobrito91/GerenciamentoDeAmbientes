function handleLoginRequest(xhr, status, args) {
	if (args.validationFailed || !args.loggedIn) {
		PF('dlgLoginAdmin').jq.effect("shake", {
			times : 5
		}, 100);
	} else {

		PF('dlgLoginAdmin').hide();
		$('#loginLink').fadeOut();
	}
}
