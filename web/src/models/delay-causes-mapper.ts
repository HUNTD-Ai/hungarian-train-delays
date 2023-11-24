export const translateDelayCauseToEnglish = (delayCause: string) => {
  switch (delayCause) {
    case 'Pálya állapota miatti késés':
      return 'Delay due to track condition';
    case 'Vonat müszaki hibája miatti késés':
      return 'Delay due to technical fault of train';
    case 'Társvasúttól átvett késés':
      return 'Delay due to schedule overrun by other railway company';
    case 'Tartózkodási ido túllépése':
      return 'Delay due to prolonged time on station*';
    case 'Csatlakozásra várás miatti késés':
      return 'Delay due to waiting for connection';
    case 'Biztositóberendezési hiba miatti késés':
      return 'Delay due to fuse failure';
    case 'Más vonat által okozott késés':
      return 'Delay caused by other train';
    default:
      return 'UNKNOWN';
  }
};
