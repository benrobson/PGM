package tc.oc.pgm.action.actions;

import org.bukkit.util.Vector;
import tc.oc.pgm.action.ActionContext;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.util.VectorUtils;
import tc.oc.pgm.util.math.Formula;

public class VelocityAction extends AbstractAction<MatchPlayer> {
  private final Formula<MatchPlayer> xformula;
  private final Formula<MatchPlayer> yformula;
  private final Formula<MatchPlayer> zformula;
  private final boolean vehicle;

  public VelocityAction(
      Formula<MatchPlayer> xformula, Formula<MatchPlayer> yformula, Formula<MatchPlayer> zformula) {
    this(xformula, yformula, zformula, false);
  }

  /**
   * @param vehicle if true and the player is riding something, the velocity is applied to the
   *     vehicle they ride instead of the player
   */
  public VelocityAction(
      Formula<MatchPlayer> xformula,
      Formula<MatchPlayer> yformula,
      Formula<MatchPlayer> zformula,
      boolean vehicle) {
    super(MatchPlayer.class);
    this.xformula = xformula;
    this.yformula = yformula;
    this.zformula = zformula;
    this.vehicle = vehicle;
  }

  @Override
  public void trigger(MatchPlayer matchPlayer, ActionContext context) {
    double x = xformula.apply(matchPlayer, context.frame());
    double y = yformula.apply(matchPlayer, context.frame());
    double z = zformula.apply(matchPlayer, context.frame());
    Vector velocity = VectorUtils.clampVelocityVector(new Vector(x, y, z));
    org.bukkit.entity.Entity ridden = matchPlayer.getBukkit().getVehicle();
    if (vehicle && ridden != null) {
      ridden.setVelocity(velocity);
    } else {
      matchPlayer.getBukkit().setVelocity(velocity);
    }
  }
}
