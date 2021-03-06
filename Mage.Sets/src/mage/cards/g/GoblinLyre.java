/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.g;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.SacrificeSourceCost;
import mage.abilities.dynamicvalue.common.PermanentsOnBattlefieldCount;
import mage.abilities.dynamicvalue.common.PermanentsTargetOpponentControlsCount;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.common.FilterCreaturePermanent;
import mage.game.Game;
import mage.players.Player;
import mage.target.common.TargetOpponent;

/**
 *
 * @author L_J
 */
public class GoblinLyre extends CardImpl {

    public GoblinLyre(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ARTIFACT},"{3}");

        // Sacrifice Goblin Lyre: Flip a coin. If you win the flip, Goblin Lyre deals damage to target opponent equal to the number of creatures you control. If you lose the flip, Goblin Lyre deals damage to you equal to the number of creatures that opponent controls.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new GoblinLyreEffect(), new SacrificeSourceCost());
        ability.addTarget(new TargetOpponent());
        this.addAbility(ability);
    }

    public GoblinLyre(final GoblinLyre card) {
        super(card);
    }

    @Override
    public GoblinLyre copy() {
        return new GoblinLyre(this);
    }
}

class GoblinLyreEffect extends OneShotEffect {

    public GoblinLyreEffect() {
        super(Outcome.Damage);
        this.staticText = "Flip a coin. If you win the flip, {this} deals damage to target opponent equal to the number of creatures you control. If you lose the flip, {this} deals damage to you equal to the number of creatures that opponent controls";
    }

    public GoblinLyreEffect(final GoblinLyreEffect effect) {
        super(effect);
    }

    @Override
    public GoblinLyreEffect copy() {
        return new GoblinLyreEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Player opponent = game.getPlayer(getTargetPointer().getFirst(game, source));
        if (controller != null) {
            if (controller.flipCoin(game)) {
                if (opponent != null) {
                    opponent.damage(new PermanentsOnBattlefieldCount(new FilterControlledCreaturePermanent()).calculate(game, source, this), source.getSourceId(), game, false, true);
                    return true;
                }
            } else {
                controller.damage(new PermanentsTargetOpponentControlsCount(new FilterCreaturePermanent()).calculate(game, source, this), source.getSourceId(), game, false, true);
                return true;
            }
        }
        return false;
    }
}
